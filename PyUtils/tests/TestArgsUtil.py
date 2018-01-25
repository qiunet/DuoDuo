#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import os
import sys
import unittest
from utils import ArgsUtil


class ArgsUtilTest(unittest.TestCase):
    def test(self):
        sys.argv.append('--port=6379')
        sys.argv.append('--host=localhost')

        argsMap = ArgsUtil.ArgsDict()
        self.assertEqual(argsMap.getInt('port'), 6379)
        self.assertEqual(argsMap.getInt('ports', 0), 0)
        self.assertEqual(argsMap.getStr('host'), 'localhost')
        self.assertEqual(argsMap.containKey('ports'), False)

