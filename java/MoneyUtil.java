public class MoneyUtil {

    /**
     * 小数精确范围
     */
    private static final int MONEY_SCALE = 2;

    /**
     * 四舍五入
     */
    private static final RoundingMode MONEY_ROUND = RoundingMode.HALF_UP;

    /**
     * 税率百分比Formatter
     */
    private static final DecimalFormat TAX_PERCENT_FORMAT = new DecimalFormat("#.##%");

    /**
     * 元到分的进制转换
     */
    public static final int YUAN_FEN = 100;

    /**
     * 提供精确加法计算方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return add(b1, b2).doubleValue();
    }

    /**
     * 提供精确加法计算方法（BigDecimal）
     *
     * @param b1 被加数 BigDecimal
     * @param b2 加数 BigDecimal
     * @return 两个参数的和 BigDecimal
     */
    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        return b1.add(b2).setScale(MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 将传入的所有double类数值相加并返回结果
     *
     * @param values double类型数组
     * @return 所有数相加的和（小数仅保留两位）
     */
    public static double addAll(Object... values) {
        return sum(values).doubleValue();
    }

    /**
     * 将传入的所有double或BigDecimal类数值相加并返回结果
     *
     * @param values double类型数组
     * @return 所有数相加的和（小数仅保留两位, BigDecimal）
     */
    public static BigDecimal sum(Object... values) {
        BigDecimal sum = BigDecimal.ZERO;
        if (values != null && values.length > 0) {
            for (Object value : values) {
                if (value instanceof Double) {
                    sum = sum.add(new BigDecimal((Double) value));
                } else if (value instanceof BigDecimal) {
                    sum = sum.add((BigDecimal) value);
                }
            }
        }
        return sum.setScale(MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 提供精确减法运算方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return sub(b1, b2).doubleValue();
    }

    /**
     * 提供精确减法运算方法（BigDecimal）
     *
     * @param b1 被减数 BigDecimal
     * @param b2 减数 BigDecimal
     * @return 两个参数的差 BigDecimal
     */
    public static BigDecimal sub(BigDecimal b1, BigDecimal b2) {
        return b1.subtract(b2).setScale(MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 提供精确乘法运算方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double multiply(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return multiply(b1, b2).doubleValue();
    }

    /**
     * 提供精确乘法运算方法（BigDecimal）
     *
     * @param b1 被乘数 BigDecimal
     * @param b2 乘数 BigDecimal
     * @return 两个参数的积 BigDecimal
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        return b1.multiply(b2).setScale(MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 提供精确乘法运算方法（BigDecimal）
     *
     * @param b1 被乘数 BigDecimal
     * @param b2 乘数
     * @return 两个参数的积 BigDecimal
     */
    public static BigDecimal multiply(BigDecimal b1, double b2) {
        if (b1 == null) {
            return BigDecimal.ZERO;
        }
        return b1.multiply(BigDecimal.valueOf(b2)).setScale(MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 提供精确乘法运算方法
     *
     * @param b1 被乘数
     * @param b2 乘数 BigDecimal
     * @return 两个参数的积的正数部分
     */
    public static long multiply(long b1, BigDecimal b2) {
        return BigDecimal.valueOf(b1).multiply(b2).setScale(MONEY_SCALE, MONEY_ROUND).longValue();
    }

    /**
     * 提供精确的除法运算方法（四舍五入）
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale 小数精确范围
     * @return 两个参数的商
     */
    public static double divideWithRound(double value1, double value2, int scale) {
        // 如果精确范围小于0，默认保留两位
        if (scale < 0) {
            scale = MONEY_SCALE;
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 金额除法运算（四舍五入，小数点后保留两位）
     *
     * @param value1 被除数
     * @param value2 除数
     * @return 两个参数的商
     */
    public static double moneyDivide(double value1, double value2) {
        return divideWithRound(value1, value2, MONEY_SCALE);
    }

    /**
     * 金额除法运算，四舍五入，小数点后保留两位，（BigDecimal，指定小数位和舍入方式）
     *
     * @param value1 被除数 BigDecimal
     * @param value2 除数 BigDecimal
     * @param scale 小数位
     * @param roundingMode 舍入方式
     * @return 两个参数的商 BigDecimal
     */
    public static BigDecimal bigDecimalDivide(BigDecimal value1, BigDecimal value2, int scale,
            RoundingMode roundingMode) {
        if (scale < 0) {
            scale = MONEY_SCALE;
        }
        if (roundingMode == null) {
            roundingMode = MONEY_ROUND;
        }
        return value1.divide(value2, scale, roundingMode);
    }

    /**
     * 金额除法运算，四舍五入，小数点后保留两位，（BigDecimal）
     *
     * @param value1 被除数 BigDecimal
     * @param value2 除数 BigDecimal
     * @return 两个参数的商 BigDecimal
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2) {
        return bigDecimalDivide(value1, value2, MONEY_SCALE, MONEY_ROUND);
    }

    public static BigDecimal divide(BigDecimal value1, double value2) {
        BigDecimal b2 = new BigDecimal(value2);
        return bigDecimalDivide(value1, b2, MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 元转分
     *
     * @param yuan
     * @return
     */
    public static long convertYuanToFen(BigDecimal yuan) {
        return yuan.multiply(BigDecimal.valueOf(YUAN_FEN)).longValue();
    }

    /**
     * 分转元
     *
     * @param fen
     * @return
     */
    public static BigDecimal convertFenToYuan(long fen) {
        return divide(BigDecimal.valueOf(fen), YUAN_FEN);
    }

    /**
     * 根据含税金额计算不含税金额
     *
     * @param taxPrice 含税金额
     * @param tax 税率
     * @return
     */
    public static BigDecimal calculateNoTaxPrice(BigDecimal taxPrice, double tax) {
        return taxPrice.divide(BigDecimal.valueOf(1 + tax), MONEY_SCALE, MONEY_ROUND);
    }

    /**
     * 格式化税率为百分比
     */
    public static String convertDoubleToPercent(double tax) {
        return TAX_PERCENT_FORMAT.format(tax);
    }

    /**
     * 计算部分占总量的比例
     *
     * @param partCount 部分
     * @param total 总数
     * @return 返回比例
     */
    public static BigDecimal calculatePercent(double partCount, double total) {
        return BigDecimal.valueOf(partCount / total);
    }
}
