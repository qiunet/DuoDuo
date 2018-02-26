# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import re
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


def subString(string: str, start: int, end: int, step: int = 1) -> str:
    """
    字符串切分    
    :param string: 需要切分的字符串 
    :param start: 开始
    :param end: 
    :param step: 步长
    :return: 
    """
    return CommonUtil.subObj(string, start, end, step)


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


def bytesToStr(bytes: bytes, encoding: str='UTF-8') -> str:
    """
    转换bytes 到字符串
    :param bytes:
    :param encoding:
    :return:
    """
    return bytes.decode(encoding)


def strToBytes(string: str, encoding: str="UTF-8") -> bytes:
    """
    字符串 转 bytes
    :param string:
    :param encoding:
    :return:
    """
    return string.encode(encoding)


def strRealSize(string: str) -> int:
    """
    str占用真是byte的长度
    :param string:
    :return:
    """
    return len(strToBytes(string))


def formatStr(string: str, replaces: tuple) -> str:
    """
    格式化字符
    format('Hi, %s, you have $%d.' , ('Michael', 1000000))
    常见的占位符号:
    %d	整数
    %f	浮点数
    %s	字符串
    %x	十六进制整数
    :param string: 字符串
    :param replaces: 替换的元组
    :return: 格式化后的字符串
    """
    return string % replaces


def matchRegex(regex: str, string: str)-> bool:
    """
    是否匹配正则表达式
    :param regex:  最好加前缀 r
    :param string: 匹配的字符串
    :return: bool
    """
    return re.match(regex, string)
