package com.lion.formatter.expressionformatter;

import com.lion.formatter.ExpressionEnum;
import com.lion.utils.CusStack;

import java.util.*;

/**
 * 表达式格式化
 */
public class ExpressionFormatter {

    /**
     * 将表达式按内容分开转换成列表
     */
    public static List<ExpItem> divideExpression(String strExp) {
        if (strExp == null || strExp.isBlank()) {
            return new ArrayList<>();
        }
        List<ExpItem> resultList = new ArrayList<>();
        char[] arrExp = strExp.toCharArray();

        String flag = "";
        ExpressionEnum flagTyp = null;
        for (char item : arrExp) {
            ExpressionEnum charTyp = ExpressionEnum.getCharTyp(item);
            if ("".equals(flag)) {
                flag = flag + item;
                flagTyp = charTyp;
            } else if (charTyp.isCanMerge() && charTyp == flagTyp) {
                flag = flag + item;
            } else {
                ExpItem expItem = new ExpItem(flagTyp, flag);
                resultList.add(expItem);
                flag = item + "";
                flagTyp = charTyp;
            }
        }
        ExpItem expItem = new ExpItem(flagTyp, flag);
        resultList.add(expItem);
        return resultList;
    }

    /**
     * 将中缀表达式转成前缀表达式(只支持逻辑运算)
     */
    public static List<ExpItem> transferInfix2Prefix(List<ExpItem> infixExp) {
        List<ExpItem> prefixExp = new ArrayList<>();
        CusStack<ExpItem> operatorStack = new CusStack<>();
        for (int i = infixExp.size() - 1; i >= 0; i --) {
            ExpItem expItem = infixExp.get(i);
            // 遇到操作数，压入结果栈
            if (expItem.isValue()) {
                prefixExp.add(expItem);
                continue;
            }
            // 遇到操作符，
            ExpItem lastOprItem = null;
            // - 如果遇到")"，直接压入操作符栈中
            if (expItem.is(ExpressionEnum.BRACKET_RIGHT)) {
                operatorStack.push(expItem);
            }
            // - 如果遇到 "(",将 ")" 之后的内容全部弹出写入结果栈，并将 () 丢弃
            else if (expItem.is(ExpressionEnum.BRACKET_LEFT)) {
                ExpItem oprItem = operatorStack.pop();
                while(!oprItem.is(ExpressionEnum.BRACKET_RIGHT)) {
                    prefixExp.add(oprItem);
                    oprItem = operatorStack.pop();
                }
            }
            // - 如果当前操作符列表为空，或者最后一个元素为 ')',将运算符写入操作符栈
            else if (operatorStack.size() == 0 || (lastOprItem = operatorStack.peek()).is(ExpressionEnum.BRACKET_RIGHT)) {
                operatorStack.push(expItem);
            }
            // - 当前操作符的优先级大于操作符栈栈顶操作符优先级，进操作符栈
            else if (expItem.withHigherPriority(lastOprItem)) {
                operatorStack.push(expItem);
            }
            // - 当前操作符的优先级小于等于操作符栈栈顶操作符优先级，弹出操作符栈顶优先级大于当前操作符的操作符并压入结果栈，当前操作符进操作符栈
            else {
                ExpItem oprItem = operatorStack.peek();
                while(oprItem != null && oprItem.withHigherPriority(expItem)) {
                    prefixExp.add(oprItem);
                    operatorStack.pop();
                    if (operatorStack.size() == 0) {
                        oprItem = null;
                    } else {
                        oprItem = operatorStack.peek();
                    }
                }
                operatorStack.push(expItem);
            }
        }
        while (operatorStack.size() != 0) {
            prefixExp.add(operatorStack.pop());
        }
        Collections.reverse(prefixExp);
        return prefixExp;
    }

    /**
     * 将中缀表达式转成后缀表达式(只支持逻辑运算)
     */
    public static List<ExpItem> transferInfix2Suffix(List<ExpItem> infixExp) {
        List<ExpItem> suffixExp = new ArrayList<>();
        CusStack<ExpItem> operatorStack = new CusStack<>();
        for (int i = 0; i < infixExp.size(); i ++) {
            ExpItem expItem = infixExp.get(i);
            // 遇到操作数，压入结果栈
            if (expItem.isValue()) {
                suffixExp.add(expItem);
                continue;
            }
            // 遇到操作符，
            ExpItem lastOprItem = null;
            // - 如果遇到"("，直接压入操作符栈中
            if (expItem.is(ExpressionEnum.BRACKET_LEFT)) {
                operatorStack.push(expItem);
            }
            // - 如果遇到 "）",将 "（" 之后的内容全部弹出写入结果栈，并将 () 丢弃
            else if (expItem.is(ExpressionEnum.BRACKET_RIGHT)) {
                ExpItem oprItem = operatorStack.pop();
                while(!oprItem.is(ExpressionEnum.BRACKET_LEFT)) {
                    suffixExp.add(oprItem);
                    oprItem = operatorStack.pop();
                }
            }
            // - 如果当前操作符列表为空，或者最后一个元素为 '(',将运算符写入操作符栈
            else if (operatorStack.size() == 0 || (lastOprItem = operatorStack.peek()).is(ExpressionEnum.BRACKET_LEFT)) {
                operatorStack.push(expItem);
            }
            // - 当前操作符的优先级大于操作符栈栈顶操作符优先级，进操作符栈
            else if (expItem.withHigherPriority(lastOprItem)) {
                operatorStack.push(expItem);
            }
            // - 当前操作符的优先级小于等于操作符栈栈顶操作符优先级，弹出操作符栈顶优先级大于等于当前操作符的操作符并压入结果栈，当前操作符进操作符栈
            else {
                ExpItem oprItem = operatorStack.peek();
                while(oprItem != null && !oprItem.withLowerPriority(expItem)) {
                    suffixExp.add(oprItem);
                    operatorStack.pop();
                    if (operatorStack.size() == 0) {
                        oprItem = null;
                    } else {
                        oprItem = operatorStack.peek();
                    }
                }
                operatorStack.push(expItem);
            }
        }
        while (operatorStack.size() != 0) {
            suffixExp.add(operatorStack.pop());
        }
        return suffixExp;
    }


    /**
     * 计算缀后缀表达式
     */
    public void computeSuffixExp(List<ExpItem> exp) {
        List<ExpItem> oprList = new ArrayList<>();
        CusStack<String> valueList = new CusStack<>();
        for (ExpItem item : exp) {
            if (item.isValue()) {
                valueList.push(item.getItemValue());
            } else {
                String result = "";
                if (item.is(ExpressionEnum.LOGIC_NO) && valueList.size() >= 1) {
                    result = computeNo(valueList.pop());
                } else if (item.is(ExpressionEnum.LOGIC_AND) && valueList.size() >= 2) {
                    if (item.getItemValue().length() == 2) {
                        result = computeShortAnd(valueList.pop(), valueList.pop());
                    } else {
                        result = computeAnd(valueList.pop(), valueList.pop());
                    }
                } else if (item.is(ExpressionEnum.LOGIC_OR) && valueList.size() >= 2) {
                    if (item.getItemValue().length() == 2) {
                        result = computeShortOr(valueList.pop(), valueList.pop());
                    } else {
                        result = computeOr(valueList.pop(), valueList.pop());
                    }
                } else {
                    throw new IllegalArgumentException("未知的运算符");
                }
                valueList.push(result);
            }
        }
    }

    private String computeShortAnd(String a, String b) {
        boolean result = compute(a);
        // 计算 A ，如果 A 是false，直接返回，否则计算 B
        return String.valueOf(false);
    }

    private String computeShortOr(String a, String b) {
        boolean result = compute(a);
        // 计算 A ，如果 A 是true，直接返回，否则计算 B
        return String.valueOf(false);
    }

    private String computeAnd(String a, String b) {
        boolean result = compute(a);
        // 计算 A ，再计算 B
        return String.valueOf(false);
    }

    private String computeOr(String a, String b) {
        boolean result = compute(a);
        // 先计算 A, 再计算 B
        return String.valueOf(false);
    }

    private String computeNo(String a) {
        boolean result = compute(a);
        return String.valueOf(false);
    }

    private boolean compute(String a) {
        boolean result = false;
        switch(a) {
            case "true":
                result = true;
                break;
            case "false":
                result = false;
                break;
            default:
                throw new IllegalArgumentException("未知的表达式");
        }
        return result;
    }

    public static void main(String[] args) {
        List<ExpItem> compute1 = divideExpression("(JCYZG||JXSYW)&(RTLPF||PBP)");
        List<ExpItem> compute11 = transferInfix2Prefix(compute1);
        List<ExpItem> compute111 = transferInfix2Suffix(compute1);
        System.out.println("（1） " + compute1);
        System.out.println("（1） " + compute11);
        System.out.println("（1） " + compute111);
        System.out.println("-------");
        List<ExpItem> compute2 = divideExpression("(!FNDFIXERN&&PBP)||(FNDFIXERN&&(PBP||RTLPF))");
        List<ExpItem> compute22 = transferInfix2Prefix(compute2);
        List<ExpItem> compute222 = transferInfix2Suffix(compute2);
        System.out.println("（2） " + compute2);
        System.out.println("（2） " + compute22);
        System.out.println("（2） " + compute222);
        System.out.println("-------");
        List<ExpItem> compute3 = divideExpression("RTLPF");
        List<ExpItem> compute33 = transferInfix2Prefix(compute3);
        List<ExpItem> compute333 = transferInfix2Suffix(compute3);
        System.out.println("（3） " + compute3);
        System.out.println("（3） " + compute33);
        System.out.println("（3） " + compute333);
        System.out.println("-------");
        List<ExpItem> compute4 = divideExpression("RTLPF&PBP||EEE");
        List<ExpItem> compute44 = transferInfix2Prefix(compute4);
        List<ExpItem> compute444 = transferInfix2Suffix(compute4);
        System.out.println("（4） " + compute4);
        System.out.println("（4） " + compute44);
        System.out.println("（4） " + compute444);
    }

    /**
     * 表达式项的元素
     */
    static class ExpItem {
        private final ExpressionEnum itemTyp;
        private final String itemValue;

        public ExpItem(ExpressionEnum itemTyp, String itemValue) {
            this.itemTyp = itemTyp;
            this.itemValue = itemValue;
        }

        public ExpressionEnum getItemTyp() {
            return itemTyp;
        }

        public boolean isValue() {
            return "value".equals(this.itemTyp.getType());
        }

        public int getOprPriority() {
            return this.itemTyp.getOprPriority();
        }

        public boolean is(ExpressionEnum itemTyp) {
            return this.itemTyp == itemTyp;
        }

        public boolean withHigherPriority(ExpItem expItem) {
            return this.getItemTyp().getOprPriority() < expItem.getOprPriority();
        }

        public boolean withLowerPriority(ExpItem expItem) {
            return this.getItemTyp().getOprPriority() > expItem.getOprPriority();
        }

        public String getItemValue() {
            return itemValue;
        }

        @Override
        public String toString() {
            return itemValue;
        }
    }

}
