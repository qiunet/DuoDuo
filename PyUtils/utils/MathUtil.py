#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import random


def randomInt(start: int, end: int) -> int:
    """
    随机整数
    :param start: 开始数
    :param end: 结束数
    :return: 随机整数
    """
    return random.randint(start, end)


def randomFloat(start: int, end: int) -> float:
    """
    随机浮点数
    :param start: 开始数
    :param end: 结束数
    :return: 随机浮点
    """
    return random.uniform(start, end)


def isPowerOfTwo(num: int) -> bool:
    """
    判断一个数, 是否是2的幂数
    :param num: 整数
    :return: bool
    """
    return (num & -num) == num
