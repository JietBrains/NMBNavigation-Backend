import json
import os

import networkx as nx
import pandas as pd
from node import Node
from PIL import Image, ImageDraw, ImageOps, ImageFont
import pickle
import logging

G = nx.Graph()
target2NodeMap = {}
markedNodeMap = {}
config = json.load(open("config.json", "r", encoding="utf-8"))
line_width = config["line_width"]  # 线宽
line_color = config["line_color"]  # 线条颜色
circle_radius = config["circle_radius"]  # 圆圈半径
font_size = config["font_size"]  # 字体大小
elevator_width = config["elevator_width"]  # 电梯权重
floor_width = config["floor_width"]  # 楼层权重
overpass_width = config["overpass_width"]  # 天桥权重
stair_width = config["stair_width"]  # 同一楼梯权重

logger = logging.getLogger(__name__)


def init_graph():
    """
    从Excel文件中读取节点信息，构建图
    :return: None
    """

    # 从nodes.xlsx文件中读取单一楼层节点信息
    excel_file = pd.ExcelFile("data/nodes/nodes.xlsx")
    for sheet_name in excel_file.sheet_names:
        df = excel_file.parse(sheet_name)
        for _, row in df.iterrows():
            nodeName = sheet_name + "_" + str(row["nodeId"])
            neighbors = str(row["neighbors"]).split(",")
            neighborNames = [sheet_name + "_" + neighbor for neighbor in neighbors]
            node = Node(nodeName, row["x"], row["y"], neighborNames)
            if not pd.isnull(row["targets"]):
                add_node_target_map(node, row["targets"].split(","))
            G.add_node(nodeName, node=node)
        for _, row in df.iterrows():
            nodeName = sheet_name + "_" + str(row["nodeId"])
            neighbors = str(row["neighbors"]).split(",")
            for neighbor in neighbors:
                if neighbor == "":
                    continue
                neighborName = sheet_name + "_" + neighbor
                distance = cal_distance(nodeName, neighborName)
                G.add_edge(nodeName, neighborName, weight=distance)

    # 从crossFloor.xlsx文件中读取跨楼层节点信息
    excel_file = pd.ExcelFile("data/nodes/crossFloor.xlsx")
    for sheet_name in excel_file.sheet_names:
        df = excel_file.parse(sheet_name)
        if sheet_name == "elevator":
            handle_elevator_node(df)
        elif sheet_name == "stair":
            handle_stair_node(df)
        elif sheet_name == "overpass":
            handle_overpass_node(df)
        elif sheet_name == "same":
            handle_same_node(df)
    excel_file = pd.ExcelFile("data/nodes/marked.xlsx")
    for sheet_name in excel_file.sheet_names:
        df = excel_file.parse(sheet_name)
        if sheet_name == "toilet":
            handle_toilet_node(df)
        elif sheet_name == "vendingMachine":
            handle_vending_machine_node(df)
    logger.info("Graph initialized successfully.")

def handle_vending_machine_node(df):
    """
    处理自动售货机节点
    :param df: 自动售货机节点数据
    :return: None
    """
    for _, row in df.iterrows():
        if row.isnull().any():
            continue
        nodeName = row["nodeName"]
        if "vendingMachine" not in markedNodeMap.keys():
            markedNodeMap["vendingMachine"] = [nodeName]
        else:
            markedNodeMap["vendingMachine"].append(nodeName)
def handle_toilet_node(df):
    """
    处理厕所节点
    :param df: 厕所节点数据
    :return: None
    """
    for _, row in df.iterrows():
        if row.isnull().any():
            continue
        nodeName = row["nodeName"]
        if "toilet" not in markedNodeMap.keys():
            markedNodeMap["toilet"] = [nodeName]
        else:
            markedNodeMap["toilet"].append(nodeName)

def handle_elevator_node(df):
    """
    处理电梯节点
    :param df: 电梯节点数据
    :return: None
    """
    elevator2NodeMap = {}
    for _, row in df.iterrows():
        if row.isnull().any():
            continue
        nodeName1 = row["nodeName1"]
        nodeName2 = row["nodeName2"]
        if nodeName2 not in elevator2NodeMap.keys():
            elevator2NodeMap[nodeName2] = [nodeName1]
        else:
            elevator2NodeMap[nodeName2].append(nodeName1)
    for nodes in elevator2NodeMap.values():
        for node1 in nodes:
            for node2 in nodes:
                if node1 != node2:
                    G.add_edge(node1, node2, weight=elevator_width)


def handle_stair_node(df):
    """
    处理楼梯节点
    :param df: 楼梯节点数据
    :return: None
    """
    stair2NodeMap = {}
    for _, row in df.iterrows():
        if row.isnull().any():
            continue
        nodeName1 = row["nodeName1"]
        nodeName2 = row["nodeName2"]
        if nodeName2 not in stair2NodeMap.keys():
            stair2NodeMap[nodeName2] = [nodeName1]
        else:
            stair2NodeMap[nodeName2].append(nodeName1)
    for nodes in stair2NodeMap.values():
        for node1 in nodes:
            for node2 in nodes:
                if node1 != node2:
                    floor1 = node1.split("_")[0][1]
                    floor2 = node2.split("_")[0][1]
                    if abs(int(floor1) - int(floor2)) == 1:
                        G.add_edge(node1, node2, weight=floor_width)
                    else:
                        interval = abs(int(floor1) - int(floor2))
                        G.add_edge(node1, node2, weight=stair_width * (interval - 1) + floor_width)


def handle_overpass_node(df):
    """
    处理天桥节点
    :param df: 天桥节点数据
    :return: None
    """
    for _, row in df.iterrows():
        if row.isnull().any():
            continue
        nodeName1 = row["nodeName1"]
        nodeName2 = row["nodeName2"]
        G.add_edge(nodeName1, nodeName2, weight=overpass_width)
        G.add_edge(nodeName2, nodeName1, weight=overpass_width)


def handle_same_node(df):
    """
    处理同一节点
    :param df: 同一节点数据
    :return: None
    """
    for _, row in df.iterrows():
        if row.isnull().any():
            continue
        nodeName1 = row["nodeName1"]
        nodeName2 = row["nodeName2"]
        G.add_edge(nodeName1, nodeName2, weight=0)
        G.add_edge(nodeName2, nodeName1, weight=0)


def isElevatorNodeReachable(node1, node2):
    """
    判断两个电梯节点是否可以互通
    :param node1: 节点1
    :param node2: 节点2
    :return: True/False
    """
    # 2楼不能到达1楼
    if node1[0] == node2[0] and node1[1] == "2" and node2[1] == "1":
        return False
    return True


def save_graph(dirPath):
    """
    保存图到文件
    :param dirPath: 文件路径
    :return: None
    """
    graph_path = dirPath + "/graph.gpickle"
    target2Map_path = dirPath + "/target2NodeMap.gpickle"
    markedNodeMap_path = dirPath + "/markedNodeMap.gpickle"
    with open(graph_path, 'wb') as f:
        pickle.dump(G, f)
        logger.info("Graph saved successfully.")
    with open(target2Map_path, 'wb') as f:
        pickle.dump(target2NodeMap, f)
        logger.info("Target to Node Map saved successfully.")
    with open(markedNodeMap_path, 'wb') as f:
        pickle.dump(markedNodeMap, f)
        logger.info("Marked Node Map saved successfully.")

def load_graph(dirPath):
    """
    从文件加载图
    :param target2Map_path: 目标到节点映射文件路径
    :param graph_path: 图文件路径
    :return: None
    """

    graph_path = dirPath + "/graph.gpickle"
    target2Map_path = dirPath + "/target2NodeMap.gpickle"
    markedNodeMap_path = dirPath + "/markedNodeMap.gpickle"
    with open(graph_path, 'rb') as f:
        global G
        G = pickle.load(f)
        logger.info("Graph loaded successfully.")
    with open(target2Map_path, 'rb') as f:
        global target2NodeMap
        target2NodeMap = pickle.load(f)
        logger.info("Target to Node Map loaded successfully.")
    with open(markedNodeMap_path, 'rb') as f:
        global markedNodeMap
        markedNodeMap = pickle.load(f)
        logger.info("Marked Node Map loaded successfully.")

def find_nearest_path(start, type):
    """
    查找离type最近的路径
    :param node: 节点
    :return: 最短路径列表
    """
    nearest_toilet = None
    min_distance = float('inf')
    startNode: Node = target2NodeMap[start]
    for toilet in markedNodeMap[type]:
        distance = nx.shortest_path_length(G, source=startNode.id, target=toilet, weight='weight')
        if distance < min_distance:
            min_distance = distance
            nearest_toilet = toilet
    nodePath = nx.shortest_path(G, source=startNode.id, target=nearest_toilet, weight='weight')
    path_list = __split_list_by_prefix(nodePath)
    return path_list



def add_node_target_map(node, targets):
    """
    将节点与目的地的映射关系存储到字典中
    :param node:  节点
    :param targets:  目的地列表
    :return:  None
    """
    for target in targets:
        if target not in target2NodeMap:
            target2NodeMap[target] = node
        else:
            raise Exception("Target already exists")

def cal_distance(nodeName1, nodeName2):
    """
    计算两个节点之间的距离
    :param nodeName1: 节点1
    :param nodeName2: 节点2
    :return: 距离
    """
    node1 = G.nodes[nodeName1]['node']
    node2 = G.nodes[nodeName2]['node']
    distance = ((node1.x - node2.x) ** 2 + (node1.y - node2.y) ** 2) ** 0.5
    return distance


def get_shortest_path(start, end) -> list:
    """
    获取最短路径
    :return:  最短路径列表
    """
    startNode: Node = target2NodeMap[start]
    endNode: Node = target2NodeMap[end]
    # 获取最短路径和对应权重
    nodePath = nx.shortest_path(G, source=startNode.id, target=endNode.id, weight='weight')
    # print(nodePath)
    path_list = __split_list_by_prefix(nodePath)
    return path_list


def __split_list_by_prefix(lst):
    """
    根据前缀将列表分组
    :param lst:  待分组的列表
    :return:  分组后的列表
    """
    if not lst:
        return []

    result = []
    current_prefix = None
    current_group = []

    for s in lst:
        # 提取前缀（下划线前的部分）
        prefix = s.split('_')[0]

        if current_prefix is None:
            # 初始化第一个分组
            current_prefix = prefix
            current_group = [s]
        else:
            if prefix == current_prefix:
                # 相同前缀，继续当前分组
                current_group.append(s)
            else:
                # 不同前缀，保存当前分组并创建新分组
                result.append(current_group)
                current_prefix = prefix
                current_group = [s]

    # 添加最后一个分组
    result.append(current_group)
    return result


def get_node(nodeName) -> Node:
    return G.nodes[nodeName]['node']


def paint_path(path_list):
    """
    绘制路径
    :param path_list: 路径列表
    :return:  None
    """
    for root, _, files in os.walk("output", topdown=False):
        for file in files:
            file_path = os.path.join(root, file)
            try:
                os.remove(file_path)
            except Exception as e:
                logger.warning(f"删除文件 {file_path} 时出错: {e}")
    jpg_num = 0
    for index, nodePath in enumerate(path_list):
        # print(nodePath)
        if len(nodePath) > 1:
            image = Image.open("./map/" + nodePath[0].split('_')[0] + ".png")
            image = ImageOps.exif_transpose(image)
            draw = ImageDraw.Draw(image)
            # 绘制路线
            __draw_node_path(nodePath, draw)
            # 绘制起点终点标记
            __draw_circle(draw, nodePath)
            if image.mode == 'RGBA':
                image = image.convert('RGB')
            image.save(f"output/output_image_{jpg_num}.jpg")
            jpg_num += 1


def __draw_node_path(nodePath, draw):
    """
    在图像上绘制线段
    :param nodePath: 路径节点列表
    :param draw:  ImageDraw对象
    :return:  None
    """
    # 获取起点终点坐标
    start_node = nodePath[0]
    start_x, start_y = G.nodes[start_node]['node'].coordinates

    # 绘制路径线段
    current_x, current_y = start_x, start_y
    for node in nodePath[1:]:
        next_node = G.nodes[node]['node']
        next_x, next_y = next_node.coordinates
        draw.line((current_x, current_y, next_x, next_y), fill=line_color, width=line_width)
        current_x, current_y = next_x, next_y


def __draw_circle(draw, nodePath):
    """
    绘制起点终点标记
    :param draw:
    :param nodePath:
    :return:
    """
    # 获取起点终点坐标
    start_node = nodePath[0]
    end_node = nodePath[-1]
    start_x, start_y = G.nodes[start_node]['node'].coordinates
    end_x, end_y = G.nodes[end_node]['node'].coordinates
    # 绘制起点终点标记

    # 起点红色圆圈（带白色"起"）
    draw.ellipse(
        (start_x - circle_radius, start_y - circle_radius,
         start_x + circle_radius, start_y + circle_radius),
        fill="red", outline="red"
    )
    # 终点蓝色圆圈（带白色"终"）
    draw.ellipse(
        (end_x - circle_radius, end_y - circle_radius,
         end_x + circle_radius, end_y + circle_radius),
        fill="red", outline="red"
    )

    # 添加文字（需根据系统字体调整）
    try:
        font = ImageFont.truetype("simsun.ttc", font_size)
    except:
        font = ImageFont.load_default()
        print("警告：未找到中文字体，文字可能无法正常显示")

    # 起点文字
    text = "起"
    left, top, right, bottom = font.getbbox(text)
    w, h = right - left, bottom - top
    draw.text((start_x - w / 2, start_y - h / 2), text, fill="white", font=font)

    # 终点文字
    text = "终"
    left, top, right, bottom = font.getbbox(text)
    w, h = right - left, bottom - top
    draw.text((end_x - w / 2, end_y - h / 2), text, fill="white", font=font)


if __name__ == "__main__":
    # init_graph()
    # save_graph("./data/pickle/graph.gpickle", "./data/pickle/target2NodeMap.gpickle")
    load_graph("./data/pickle/graph.gpickle", "./data/pickle/target2NodeMap.gpickle")
    # start = input("请输入起点：")
    # end = input("请输入终点：")
    # path = get_shortest_path(start, end)
    # paint_path(path)
    print(G.nodes)
