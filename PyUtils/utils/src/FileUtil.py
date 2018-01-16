# -*- coding: utf-8 -*-

# 文件的工具类

import os
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


def delFile(filepath):
    """
    删除一个文件
    :param filepath: 文件绝对路径
    :return:
    """
    os.remove(filepath)


def existFile(filepath):
    """
    判断一个文件是否存在
    :param filepath: 文件绝对路径
    :return:  True 存在  False 不存在
    """
    return os.path.exists(filepath)

