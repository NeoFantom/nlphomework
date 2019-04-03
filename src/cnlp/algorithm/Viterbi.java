package cnlp.algorithm;

/**
 * 维特比算法
 * @author hankcs
 */
public class Viterbi
{
    /**
     * 求解HMM模型
     * @param observed_seq 观测序列
     * @param start_p 初始概率（隐状态）
     * @param trans_p 转移概率（隐状态）
     * @param emit_p 发射概率 （隐状态表现为显状态的概率）
     * @return 最可能的序列
     */
    public static int[] compute(int[] observed_seq,
                                double[] start_p,
                                double[][] trans_p,
                                double[][] emit_p)
    {
        int tag_count = start_p.length;
        int sequence_length = observed_seq.length;
        double[][] V = new double[sequence_length][tag_count];
        int[][] path = new int[tag_count][sequence_length];

        for (int y = 0; y < tag_count; y++)
        {
            V[0][y] = start_p[y] * emit_p[y][observed_seq[0]];
            path[y][0] = y;
        }

        for (int t = 1; t < sequence_length; ++t)
        {
            int[][] newpath = new int[tag_count][sequence_length];

            for (int y = 0; y < tag_count; y++)
            {
                double prob = -1;
                int state;
                for (int y0 = 0; y0 < tag_count; y0++)
                {
                    double nprob = V[t - 1][y0] * trans_p[y0][y] * emit_p[y][observed_seq[t]];
                    if (nprob > prob)
                    {
                        prob = nprob;
                        state = y0;
                        // 记录最大概率
                        V[t][y] = prob;
                        // 记录路径
                        System.arraycopy(path[state], 0, newpath[y], 0, t);
                        newpath[y][t] = y;
                    }
                }
            }

            path = newpath;
        }

        double prob = -1;
        int state = 0;
        for (int y = 0; y < tag_count; y++)
        {
            if (V[sequence_length - 1][y] > prob)
            {
                prob = V[sequence_length - 1][y];
                state = y;
            }
        }

        return path[state];
    }
}
