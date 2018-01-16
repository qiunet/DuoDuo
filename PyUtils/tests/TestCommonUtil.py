# -*- coding: utf-8 -*-
# 系统命令相关的python功能.

import unittest

from utils import CommonUtil


class TestCommonUtilFunc(unittest.TestCase):

    def test_is_empty(self):
        self.assertTrue(CommonUtil.isEmpty(""))
        self.assertTrue(CommonUtil.isEmpty(None))
        self.assertFalse(CommonUtil.isEmpty("me"))


if __name__ == '__main__':
    unittest.main()
