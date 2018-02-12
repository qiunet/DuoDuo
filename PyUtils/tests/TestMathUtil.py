
import unittest
from utils import MathUtil


class MathUtilFun(unittest.TestCase):

    def testSort(self):
        data = [4, 1, 3, 2]
        self.assertEqual([1, 2, 3, 4], MathUtil.sort(data))
        self.assertEqual([4, 3, 2, 1], MathUtil.sort(data, reverse=True))

        data = [4, 1, -3, 2]
        self.assertEqual([1, 2, -3, 4], MathUtil.sort(data, key=abs))
