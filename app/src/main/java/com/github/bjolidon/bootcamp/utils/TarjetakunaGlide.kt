package com.github.bjolidon.bootcamp.utils

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * This class is used to generate the GlideApp class.
 * It is mandatory by Glide,
 * otherwise Glide could create the error : Failed to find GeneratedAppGlideModule
 */
@GlideModule
class TarjetakunaGlide : AppGlideModule()
