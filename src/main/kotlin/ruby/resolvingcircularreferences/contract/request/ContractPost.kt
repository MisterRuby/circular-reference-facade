package ruby.resolvingcircularreferences.contract.request

import java.math.BigDecimal


data class ContractPost(
    val name: String, // 캠페인 이름
    val startDate: String, // 시작일
    val endDate: String, // 종료일
    val amount: BigDecimal, // 계약 금액
)
