# -*- coding: utf-8 -*-
# 日期相关的python功能.

__author__ = 'qiunet'

import datetime

DEDAULT_DATE_FORMAT = "%Y-%m-%d"
DEDAULT_TIME_FORMAT = "%H:%M:%S"
DEDAULT_DATETIME_FORMAT = "%Y-%m-%d %H:%M:%S"


def dateToString(dateTime: datetime) -> str:
    """
    使用默认的format 格式化一个时间对象
    :param dateTime: 时间对象
    :return: 格式化后的字符串
    """
    return dateToString(dateTime, DEDAULT_DATETIME_FORMAT)


def dateToString(dateTime: datetime, formatStr: str) -> str:
    """
    格式化一个日期
    :param formatStr: 日期格式 例: [%Y-%m-%d %H:%M:%S]
    :param dateTime 需要formt的时间对象
    :return:  格式化后的字符串
    """
    return dateTime.strftime(formatStr)


def stringToDate(dateString: str) -> datetime:
    """
    默认格式字符串转时间对象
    :param dateString: 时间字符串
    :return: 时间对象
    """
    return stringToDate(dateString, DEDAULT_DATETIME_FORMAT)


def stringToDate(dateString: str, formatStr: str) -> datetime:
    """
    字符串转时间对象
    :param dateString: 时间字符串
    :param formatStr: 字符串的格式
    :return: 时间对象
    """
    return datetime.datetime.strftime(dateString, formatStr)


def datetimeNow() -> datetime:
    """
    获得当前时间
    :return 当前时间
    """
    return datetime.datetime.now()

