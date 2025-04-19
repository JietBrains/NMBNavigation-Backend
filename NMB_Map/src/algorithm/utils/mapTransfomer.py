import os
from PIL import Image


def process_image(input_path, output_path):
    # 打开图片
    with Image.open(input_path) as img:
        # 处理透明背景
        if img.mode in ('RGBA', 'LA'):
            # 创建白色背景并合并
            background = Image.new('RGB', img.size, (255, 255, 255))
            background.paste(img, mask=img.split()[-1])  # 使用alpha通道作为蒙版
            img = background
        else:
            # 确保转换为RGB（例如处理P模式图像）
            img = img.convert('RGB')

        # 计算新尺寸
        new_width = img.width + 200
        new_height = img.height + 200

        # 创建新图像（白色背景）
        new_img = Image.new('RGB', (new_width, new_height), (255, 255, 255))

        # 计算粘贴位置（居中）
        paste_position = (
            (new_width - img.width) // 2,
            (new_height - img.height) // 2
        )

        # 粘贴原图到新图像
        new_img.paste(img, paste_position)

        # 保持原始格式保存（根据输出路径的扩展名）
        new_img.save(output_path)


def process_directory(source_dir, target_dir):
    # 支持的图片扩展名
    valid_extensions = ('.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp')

    for root, dirs, files in os.walk(source_dir):
        # 计算相对路径
        relative_path = os.path.relpath(root, source_dir)
        dest_dir = os.path.join(target_dir, relative_path)

        # 创建目标目录
        os.makedirs(dest_dir, exist_ok=True)

        for file in files:
            # 检查文件扩展名
            ext = os.path.splitext(file)[1].lower()
            if ext in valid_extensions:
                input_path = os.path.join(root, file)
                output_path = os.path.join(dest_dir, file)

                try:
                    process_image(input_path, output_path)
                    print(f"成功处理: {input_path}")
                except Exception as e:
                    print(f"处理失败 {input_path}: {str(e)}")


if __name__ == "__main__":
    import sys

    source_dir = "../map"
    target_dir = "../map"

    if not os.path.exists(source_dir):
        print(f"错误: 源目录不存在 {source_dir}")
        sys.exit(1)

    process_directory(source_dir, target_dir)
    print("处理完成！")