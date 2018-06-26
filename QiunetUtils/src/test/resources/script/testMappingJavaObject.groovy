#!/usr/bin/env groovyScript
package script

/***
 *
 * Create by  qiunet in 17/9/4 13:58
 * mail: qiunet@gmail.com
 ********* BODY*********
 **/
void changeUserPo(user, age) {
    user.age = age
    if (user.id < 15) {
        user.name = "qiunet ${user.id}"
    }else {
        user.name = "qiunets ${user.id}"
    }
}


