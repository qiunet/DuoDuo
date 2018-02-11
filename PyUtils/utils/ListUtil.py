#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

from utils import CommonUtil


def subList(ls: list, start: int, end: int, step: int = 1) -> list:
    """
    分割list
    :param ls: list 
    :param start: 起始数
    :param end:  结束数
    :param step: 步长
    :return: 
    """
    return CommonUtil.subObj(ls, start, end, step)

