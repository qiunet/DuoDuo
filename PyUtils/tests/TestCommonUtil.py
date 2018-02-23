# -*- coding: utf-8 -*-
# 系统命令相关的python功能.

import unittest

from utils import CommonUtil


class TestCommonUtilFunc(unittest.TestCase):
    def test_is_empty(self):
        self.assertTrue(CommonUtil.isEmpty(""))
        self.assertTrue(CommonUtil.isEmpty(None))
        self.assertFalse(CommonUtil.isEmpty("me"))

    def testSubObj(self):
        num = 1.0
        print(type(abs))
        # 异常检查判断
        self.assertRaises(Exception, CommonUtil.subObj, num, 0, 1)
        self.assertEqual([1, 3, 5], CommonUtil.subObj([1, 2, 3, 4, 5, 6], 0, 6, 2))
        self.assertEqual((1, 3, 5), CommonUtil.subObj((1, 2, 3, 4, 5, 6), 0, 6, 2))

    def testMap(self):
        nums = [1, 2, 3, 4, 5]
        ds = CommonUtil.mapList(doubles, nums)
        self.assertEqual([2, 4, 6, 8, 10], ds)

    def testFilter(self):
        nums = [1, 2, 3, 4, 5]
        # 使用了匿名函数
        ds = CommonUtil.filterList(lambda x: x % 2 == 1, nums)
        self.assertEqual([1, 3, 5], ds)

    def testAttr(self):
        print(CommonUtil.listAllAttr('123'))


def doubles(x):
    return x * 2
