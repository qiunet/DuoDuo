# -*- coding: utf-8 -*-

# 文件的工具类

__author__ = 'qiunet'


def readContent(filename):
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



