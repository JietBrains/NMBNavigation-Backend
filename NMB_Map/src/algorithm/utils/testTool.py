import pandas as pd
from PIL import Image, ImageDraw, ImageFont, ImageOps

excel = "../data/nodes/nodes.xlsx"


def printOnMap(mapImage, sheetName):
    """
    在地图上打印点和连线，用来检验标注数据是否正确
    :param mapImage: 想要绘制的地图图片
    :param sheetName:  Excel表格的sheet名
    :return:
    """

    # 1. 读取Excel数据
    df = pd.read_excel(excel, sheet_name=sheetName)

    # 2. 加载图片
    image = Image.open("../map/" + mapImage)
    image = ImageOps.exif_transpose(image)
    draw = ImageDraw.Draw(image)

    # 3. 配置绘制参数
    point_color = "red"  # 点颜色
    point_radius = 20  # 点半径（圆形）
    text_color = "yellow"  # 文字颜色
    font_size = 50  # 字体大小
    font = ImageFont.truetype("arial.ttf", font_size)  # 使用系统字体或指定路径

    node2Coord = {}

    # 4. 遍历所有点进行绘制
    for _, row in df.iterrows():
        x, y = int(row["x"]), int(row["y"])
        id_text = str(row["nodeId"])
        node2Coord[row["nodeId"]] = (x, y)

        # 绘制圆形点（圆心坐标，外接矩形）
        bbox = [
            (x - point_radius, y - point_radius),
            (x + point_radius, y + point_radius)
        ]
        draw.ellipse(bbox, fill=point_color)

        # 在点右侧显示ID（偏移量可调整）
        text_position = (x + point_radius + 5, y - font_size // 2)
        draw.text(text_position, id_text, fill=text_color, font=font)

    for _, row in df.iterrows():
        neighbors = str(row["neighbors"]).split(",")
        for neighbor in neighbors:
            if neighbor == "":
                continue
            x1, y1 = node2Coord[row["nodeId"]]
            x2, y2 = node2Coord[int(neighbor)]
            draw.line((x1, y1, x2, y2), fill="blue", width=5)


    # 5. 保存结果（或直接显示）
    try:
        # 如果图像模式是RGBA，则转换为RGB
        if image.mode == 'RGBA':
            image = image.convert('RGB')
        # 保存图像
        image.save("output_image.jpg")
    except Exception as e:
        print(f"保存图像时出现错误: {e}")
    for index, row in df.iterrows():
        print(row['nodeId'], row['x'], row['y'])
    return


if __name__ == "__main__":
    printOnMap("G3.png", "G3")
