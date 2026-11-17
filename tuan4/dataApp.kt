package com.example.btuan4.model

class AppRepository {
    fun getAppInfo() = dataInfor(
        name = "UTH Todo",
        tagline = "Việc học, việc nhà – gọn gàng mỗi ngày",
        version = "Phiên bản 2.1.0"
    )

    fun getOnboardingItems() = listOf(
        OnboardingItem(
            "Mọi việc, gọn trong một chỗ",
            "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
            IllustrationType.CHECKLIST
        ),
        OnboardingItem(
            "Chia theo Học tập và Cuộc sống",
            "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
            IllustrationType.CATEGORIES
        ),
        OnboardingItem(
            "Nhắc đúng lúc",
            "Bật thông báo để UTH Todo nhắc bạn trước hạn chót.",
            IllustrationType.REMINDER
        )
    )
}