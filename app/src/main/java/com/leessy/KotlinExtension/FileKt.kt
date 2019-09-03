package com.leessy.KotlinExtension

import java.io.File

/**
 *
 * @author Created by 刘承. on 2019/8/16
 *
 * --深圳市尚美欣辰科技有限公司.
 */

/**
 * 检查目录是否存在,不存在创建
 */
fun File.check_mkdirs() {
    if (!exists()) {
        mkdirs()
    }
}
