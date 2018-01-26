# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import logging

from utils import CommonUtil


def isEmpty(string: str) -> bool:
    """
    判断空对象
    :param string:
    :return: True False
    """
    return CommonUtil.isEmpty(string)


def split(string: str, sp: str) -> str():
    """
    分割字符串
    严格按照 sp 分割的
    ',,' 会分割为 ['', '', '']
    :param string: 需要分割的字符
    :param sp: 分割符号
    :return: 分割后的数组
    """
    return str(string).split(sp)


def arrayToStr(listObj: list, sep: str=',', start: str='', end: str=''):
    """
    对数组进行拼串
    :param listObj:  数组
    :param sep: 分隔符
    :param start: 开始符号
    :param end: 结束符号
    :return: 结果
    """
    string = "" + start
    if not isinstance(listObj, list):
        logging.error("%s is not list", type(listObj))
        return

    for i in range(len(listObj) - 1):
        string += (str(listObj[i]) + sep)

    return string + str(listObj[len(listObj) - 1]) + end




