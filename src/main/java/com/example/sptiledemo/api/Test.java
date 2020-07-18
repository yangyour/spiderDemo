//package com.example.sptiledemo.api;
//
//public enum Test {
//    // 可用于数字输入框
//    LESS_THAN(1, "小于", "可用于数字输入框", null),
//    GREATER_THAN(2, "大于", "可用于数字输入框", null),
//    LESS_THAN_OR_EQUAL(3, "小于等于", "可用于数字输入框", null),
//    GREATER_THAN_OR_EQUAL(4, "大于等于", "可用于数字输入框", null),
//    EQUAL(5, "等于", "可用于数字输入框", null),
//    BETWEEN(6, "介于", "可用于数字输入框", null),
//
//    // 可用于发起人、单选框、部门、人员选择
//    BELONG_TO(7, "", "可用于发起人、单选框、部门、人员选择", null),
//
//    // 可用于多选框
//    ALL_EQUAL(8, "完全等于", "可用于多选框", null),
//    JUST_ANY(9, "包含任意", "可用于多选框", null),
//
//    // 可用于客户  1、客户类型
//    CUSTOMER_TYPE(10, "客户类型", "可用于关联客户", Arrays.asList(
//            OperatorOption.POTENTIAL_CUSTOMERS,
//            OperatorOption.INVESTED_CUSTOMERS
//    )),
//    // 可用于客户  2、客户状态
//    CUSTOMER_STATUS(11, "客户状态", "可用于关联客户", Arrays.asList(
//            OperatorOption.PRELIMINARY_NEGOTIATION,
//            OperatorOption.COOPERATION_REACHED,
//            OperatorOption.SIGNING_AGREEMENT
//    )),
//
//    // 会议截止时间
//    MEETING_ABORT_TIME(12, "会议截止时间", "可用于关联会议", null),
//
//    // 审批结果
//    APPROVAL_STATUS(13, "审批结果", "可用于关联审批单", Arrays.asList(
//            OperatorOption.HAS_PASSED,
//            OperatorOption.NOT_PASSED,
//            OperatorOption.CANCEL
//    )),
//
//    // 投票结果
//    VOTE_STATUS(14, "投票", "可用于关联投票", Arrays.asList(
//            OperatorOption.AGREE,
//            OperatorOption.DISAGREE,
//            OperatorOption.DEFER_VOTE,
//            OperatorOption.CONDITIONAL_PASS
//    )),
//    ;
//
//    /**
//     * 操作符唯一标识
//     */
//    private int oprId;
//    /**
//     * 操作符名称
//     */
//    private String oprName;
//    /**
//     * 操作符描述
//     */
//    private String oprDescr;
//    /**
//     * 选项
//     */
//    private List<OperatorOption> options;
//
//    public static ProcessOperator fromCode(int oprId) {
//
//        ProcessOperator[] processOperators = ProcessOperator.values();
//        for (ProcessOperator processOperator : processOperators) {
//            if (processOperator.getOprId() == oprId) {
//                return processOperator;
//            }
//        }
//        return null;
//    }
//}
