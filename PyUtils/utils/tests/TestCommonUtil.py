# -*- coding: utf-8 -*-
# 系统命令相关的python功能.

import unittest
from utils.src.CommonUtil import *


class TestCommonUtilFunc(unittest.TestCase):

    def test_is_empty(self):
        self.assertTrue(isEmpty(""))
        self.assertTrue(isEmpty(None))
        self.assertFalse(isEmpty("me"))


if __name__ == '__main__':
    unittest.main()
