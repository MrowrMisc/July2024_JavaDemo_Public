package org.mrowrpurr.demo.websocketserver

import org.springframework.stereotype.Service

@Service
class LastMessageSent {
    var lastMessageSent: String? = null
}