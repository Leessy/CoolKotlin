package com.aiface.uvccamera.camera

import java.nio.ByteBuffer

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */
interface IFrameCall {
    fun call(bf: ByteBuffer, w: Int, h: Int)
}