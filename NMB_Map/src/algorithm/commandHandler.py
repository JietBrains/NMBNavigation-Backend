from graph import init_graph, save_graph, load_graph, get_shortest_path, get_node, paint_path, find_nearest_toilet_path
from node import Node
import logging
import json

logger = logging.getLogger(__name__)
debug_mode = json.load(open("config.json"))["debug_mode"]
class CommandHandler:
    _instance = None
    loaded = False

    def __init__(self):

        self.commands = {
            "exit": self.handle_exit,
            "search": self.handle_search_path,
            "update": self.update_graph,
            "nearest": self.handle_search_nearest,
        }

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def handle_exit(self):
        """退出程序"""
        # 修改为使用 logger 对象
        logger.info("Exiting the program.")
        return True

    def handle_search_path(self, start, end):
        """搜索路径"""
        if not self.loaded:
            load_graph("data/pickle")
            self.loaded = True
        paths = get_shortest_path(start, end)
        self.__strandardize_path(paths)
        return False

    def handle_search_nearest(self, start, type = "toilet"):
        """搜索最近的厕所"""
        if not self.loaded:
            load_graph("data/pickle")
            self.loaded = True
        if type == "toilet":
            paths = find_nearest_toilet_path(start)
            self.__strandardize_path(paths)
            return False

    def __strandardize_path(self, paths):
        pathMap = []
        drawPaths = [] if debug_mode else None
        for path in paths:
            if len(path) <= 1:
                continue
            building = path[0].split("_")[0]
            sameLayerData = {"building": building, "path": []}
            drawPath = []
            for i in range(len(path)):
                node: Node = get_node(path[i])
                # 只记录起点，终点和拐点
                if i == 0 or i == len(path) - 1:
                    sameLayerData["path"].append(node.coordinates)
                    drawPath.append(path[i])
                else:
                    forwardNode: Node = get_node(path[i - 1])
                    nextNode: Node = get_node(path[i + 1])
                    if forwardNode.x == node.x and nextNode.x == node.x:
                        continue
                    if forwardNode.y == node.y and nextNode.y == node.y:
                        continue
                    sameLayerData["path"].append(node.coordinates)
                    drawPath.append(path[i])
            pathMap.append(sameLayerData)
            if debug_mode:
                drawPaths.append(drawPath)
        print(pathMap)
        # 修改为使用 logger 对象
        logger.info("Path search completed.")
        # 绘制路径
        if debug_mode:
            paint_path(drawPaths)
        return pathMap

    def update_graph(self):
        """重新保存图"""
        init_graph()
        save_graph("data/pickle")
        load_graph("data/pickle")
        self.loaded = True
        # 修改为使用 logger 对象
        logger.info("Graph updated and saved.")
        return False

    def run(self):
        while True:
            command = input().strip().split()
            if not command:
                continue
            cmd = command[0]
            args = command[1:]
            if cmd in self.commands:
                if self.commands[cmd](*args):
                    break
            else:
                print("unknown command:", cmd)
                # 修改为使用 logger 对象
                logger.warning(f"Unknown command: {cmd}")