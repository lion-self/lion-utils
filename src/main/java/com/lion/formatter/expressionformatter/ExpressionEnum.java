package com.lion.formatter;

/**
 * @author laiyuan
 */
public enum ExpressionEnum {
    // 表达式值
    VALUE("value", 0, true),
    // 左括号
    BRACKET_LEFT("operator", 99, false),
    // 右括号
    BRACKET_RIGHT("operator", 99, false),
    // 逻辑运算符 非
    LOGIC_NO("operator", 1, false),
    // 逻辑运算符 and
    LOGIC_AND("operator",  2, true),
    // 逻辑运算符 or
    LOGIC_OR("operator", 2, true);

    /**
     * 字符类型
     */
    private final String type;
    /**
     * 操作符优先级
     */
    private final int oprPriority;
    /**
     * 是否可以和同类型字符合并成一个字符
     */
    private final boolean canMerge;

    ExpressionEnum(String type, int oprPriority, boolean canMerge) {
        this.type = type;
        this.oprPriority = oprPriority;
        this.canMerge = canMerge;
    }

    public static ExpressionEnum getCharTyp(char item) {
        if (isNumber(item) || isLetter(item)) {
             return ExpressionEnum.VALUE;
        }
        ExpressionEnum result;
         switch(item) {
             case '&':
                 result = ExpressionEnum.LOGIC_AND;
                 break;
             case '|':
                 result = ExpressionEnum.LOGIC_OR;
                 break;
             case '!':
                 result = ExpressionEnum.LOGIC_NO;
                 break;
             case '(':
                 result = ExpressionEnum.BRACKET_LEFT;
                 break;
             case ')':
                 result = ExpressionEnum.BRACKET_RIGHT;
                 break;
             default:
                 throw new IllegalArgumentException("不支持的字符类型");
         }

        return result;
    }

    public String getType() {
        return type;
    }

    public int getOprPriority() {
        return oprPriority;
    }

    public boolean isCanMerge() {
        return canMerge;
    }

    private static boolean isNumber(char item) {
        return item >= '0' && item <= '9';
    }

    private static boolean isLetter(char item) {
        return (item >= 'A' && item <= 'Z') || (item >= 'a' && item <= 'z');
    }

}
