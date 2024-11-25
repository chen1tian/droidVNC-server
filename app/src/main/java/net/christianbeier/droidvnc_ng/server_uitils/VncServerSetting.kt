package net.christianbeier.droidvnc_ng.server_uitils

data class VncServerSetting(
    /**
     * 服务端端口
     */
    val port: Int = 5900,
    /**
     * 服务端密码
     */
    val password: String?,
    /**
     * 是否允许文件传输
     */
    val fileTransfer: Boolean,
    /**
     * 是否只读
     */
    val viewOnly: Boolean,
    /**
     * 是否显示指针
     */
    val showPointers: Boolean,
    /**
     * 缩放比例
     */
    val scaling: Float = 1f,
    /**
     * 访问密钥
     */
    val accessKey: String?
)