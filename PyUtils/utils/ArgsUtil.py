# -*- coding: utf-8 -*-

# 接收参数的一个工具类

import sys
__author__ = 'qiunet'


class ArgsDict:
    """
    参数解析
    参数格式: --ip=192.168.1.102 --port=3306
    """
    __dict = dict()

    def __init__(self):
        for argObj in sys.argv:
            arg = str(argObj)
            if not arg.startswith("--"):
                continue
            args = arg.split("=")
            self.__dict.setdefault(args[0][2:], args[1])

    def getStr(self, argumentName: str, defaultVal: str='') -> str:
        """
        得到参数对应的值. ,没有. 默认为 None
        :param argumentName:  参数名
        :param defaultVal 默认值
        :return:  对应的值
        """
        if not self.containKey(argumentName):
            return defaultVal

        return self.__dict.get(argumentName, None)

    def getInt(self, argumentName: str, defaultVal: int=0) -> int:
        """
        得到参数对应的值. ,没有. 默认为 None
        :param argumentName:  参数名
        :param defaultVal 默认值
        :return:  对应的值
        """
        return int(self.getStr(argumentName, str(defaultVal)))

    def containKey(self, argumentName: str) -> bool:
        """
        判断是否有改值.
        :param argumentName:
        :return:
        """
        return argumentName in self.__dict.keys()
