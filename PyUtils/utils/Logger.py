def log(func):
    def wrapper(*args, **kw):
        print("Called: %s, params: %s , kw: %s" % (func.__name__, str(args), str(**kw)))
        return func(*args, **kw)
    return wrapper