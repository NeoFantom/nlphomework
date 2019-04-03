import os
import zipfile


def convert(path):
    """
    Compress a directory using zip
    :param path: to be compressed
    :param output_zipfile: path to output zip file
    :return:
    """
    count = 0
    for root, directories, files in os.walk(path):
        if not os.path.exists(root.replace(path, path + 'utf8')):
            os.mkdir(root.replace(path, path + 'utf8'))

        for filename in files:
            with open(root + '/' + filename, encoding='gbk') as input:
                with open(root.replace(path, path + 'utf8') + '/' + filename, mode='w', encoding='utf8') as output:
                    output.writelines(input.readlines())
                    count += 1
            # print('inner')
    return


if __name__ == '__main__':
    convert('D:/All/Stella/For NLP/Viterbi-master/data')
