import os

from commandHandler import CommandHandler

from logger_config import setup_logger

# 初始化日志记录器
logger = setup_logger()
# 设置当前工作目录
current_dir = os.path.dirname(os.path.abspath(__file__))

os.chdir(current_dir)
if __name__ == "__main__":
    logger.info("the program is starting")
    command_handler = CommandHandler()
    command_handler.run()
    logger.info("the program is finished")
