package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.user.User
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
    var isReturn: Boolean,
    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
) {
    fun doReturn() {
        this.isReturn = true;
    }
}