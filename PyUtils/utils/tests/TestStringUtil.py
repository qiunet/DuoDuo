# -*- coding: utf-8 -*-
# 系统命令相关的python功能.

import unittest
from utils.src.StringUtil import *


class TestStringUtilFunc(unittest.TestCase):
    def testIsEmpty(self):
        self.assertTrue(isEmpty(""))
        self.assertTrue(isEmpty(None))
        self.assertFalse(isEmpty("me"))

    def testSplit(self):
        strs = split(",,", ",")
        for i in range(len(strs)):
            self.assertEqual("", strs[i])

        strs = split("1,,", ",")
        self.assertEqual("1", strs[0])
        self.assertEqual("", strs[1])
        self.assertEqual("", strs[2])

        strs = split("1,,1", ",")
        self.assertEqual("1", strs[0])
        self.assertEqual("", strs[1])
        self.assertEqual("1", strs[2])

    def testArrayToString(self):
        list = [1, 2, 3]
        self.assertEqual("1,2,3", arrayToStr(list, ","))
        self.assertEqual("[1,2,3]", arrayToStr(list, ",", "[", "]"))


if __name__ == '__main__':
    unittest.main()
