package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus.LOANED
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus.RETURNED
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class UserLoanHistory(
    @ManyToOne(fetch = LAZY) val user: User,
    val bookName: String,
    var status: UserLoanStatus,
    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
) {

    val isReturn: Boolean
        get() = this.status == RETURNED

    fun doReturn() {
        this.status = RETURNED
    }

    companion object {
        fun fixture(
            user: User,
            bookName: String = "이상한 나라의 엘리스",
            status: UserLoanStatus = LOANED,
        ): UserLoanHistory {
            return UserLoanHistory(
                user = user,
                bookName = bookName,
                status = status,
            )
        }
    }
}