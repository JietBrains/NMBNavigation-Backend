from commandHandler import CommandHandler

from logger_config import setup_logger

# 初始化日志记录器
logger = setup_logger()

if __name__ == "__main__":
    logger.info("the program is starting")
    command_handler = CommandHandler()
    command_handler.run()
    logger.info("the program is finished")
