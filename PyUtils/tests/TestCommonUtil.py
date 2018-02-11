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
        # 异常检查判断
        self.assertRaises(Exception, CommonUtil.subObj, num, 0, 1)
