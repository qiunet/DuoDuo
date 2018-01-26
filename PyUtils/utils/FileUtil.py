# -*- coding: utf-8 -*-

# 文件的工具类

import os
__author__ = 'qiunet'


class Properties:
    def __init__(self, fileName: str):
        """
        初始化
        :param fileName: 文件名
        """
        self.fileName = fileName
        self.__properties = {}
        try:
            file = open(fileName, mode='r', encoding='utf-8')
            for text in file:
                line = text.strip()
                if line.startswith('#') or line.find('=') <= 0:
                    continue
                strs = line.split('=')
                self.__properties[strs[0].strip()] = strs[1].strip()

        except Exception as e:
            raise e
        finally:
            file.close()

    def containKey(self, key: str) -> bool:
        """
        是否包含key
        :param key:
        :return:
        """
        return key in self.__properties.keys()

    def get(self, key: str, defaultValue: str=None) -> str:
        """
        得到指定key的值
        :param key:
        :param defaultValue:
        :return:
        """
        if not self.containKey(key):
            return defaultValue
        return self.__properties[key]


def readContent(filename: str) -> str:
    """
    读取一个文件的全部内容
    :param filename: 文件名
    :return: 所有的内容
    """
    file = open(filename, mode='r', encoding="utf-8")
    try:
        all_content = file.read()
        return all_content
    finally:
        file.close()


def delFile(filepath: str):
    """
    删除一个文件
    :param filepath: 文件绝对路径
    :return:
    """
    os.remove(filepath)


def existFile(filepath: str) -> bool:
    """
    判断一个文件是否存在
    :param filepath: 文件绝对路径
    :return:  True 存在  False 不存在
    """
    return os.path.exists(filepath)

