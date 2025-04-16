import logging

def setup_logger():
    # 获取根日志记录器
    logger = logging.getLogger()
    # 设置日志级别
    logger.setLevel(logging.DEBUG)

    # 创建一个文件处理器
    file_handler = logging.FileHandler('app.log')
    file_handler.setLevel(logging.INFO)

    # 创建一个格式化器并添加到处理器
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    file_handler.setFormatter(formatter)

    # 将处理器添加到日志记录器
    logger.addHandler(file_handler)

    return logger