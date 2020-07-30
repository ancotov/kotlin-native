/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed -> in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.konan.target

interface TargetableExternalStorage {
    fun targetString(key: String): String?
    fun targetList(key: String): List<String>
    fun hostString(key: String): String?
    fun hostList(key: String): List<String>
    fun hostTargetString(key: String): String?
    fun hostTargetList(key: String): List<String>
    fun absolute(value: String?): String
    fun downloadDependencies()

    fun readProperty(name: String): String

    // TODO: Track circular deps.
    fun String.resolveValue(): String = when {
        startsWith("$") -> readProperty(drop(1).resolveValue()).resolveValue()
        else -> this
    }

    fun resolve(value: String): String =
            value.resolveValue()
}

interface ClangFlags : TargetableExternalStorage {
    val clangFlags get()        = targetList("clangFlags")
    val clangNooptFlags get()   = targetList("clangNooptFlags")
    val clangOptFlags get()     = targetList("clangOptFlags")
    val clangDebugFlags get()   = targetList("clangDebugFlags")
    val clangDynamicFlags get() = targetList("clangDynamicFlags")
}

interface LldFlags : TargetableExternalStorage {
    val lldFlags get()      = targetList("lld")
}

interface Configurables : TargetableExternalStorage {

    val target: KonanTarget

    val llvmHome get() = hostString("llvmHome")
    val llvmVersion get() = hostString("llvmVersion")
    val libffiDir get() = hostString("libffiDir")

    // TODO: Delegate to a map?
    val linkerOptimizationFlags get() = targetList("linkerOptimizationFlags")
    val linkerKonanFlags get() = targetList("linkerKonanFlags")
    val linkerNoDebugFlags get() = targetList("linkerNoDebugFlags")
    val linkerDynamicFlags get() = targetList("linkerDynamicFlags")
    val targetSysRoot get() = targetString("targetSysRoot")

    // Notice: these ones are host-target.
    val targetToolchain get() = hostTargetString("targetToolchain")?.resolveValue()

    val absoluteTargetSysRoot get() = absolute(targetSysRoot)
    val absoluteTargetToolchain get() = absolute(targetToolchain)
    val absoluteLlvmHome get() = absolute(llvmHome?.resolveValue())
}

interface TargetableConfigurables : Configurables {
    val targetArg get() = targetString("quadruple")
}

interface AppleConfigurables : Configurables, ClangFlags {
    val arch get() = targetString("arch")!!
    val osVersionMin get() = targetString("osVersionMin")!!
    val osVersionMinFlagLd get() = targetString("osVersionMinFlagLd")!!
    val additionalToolsDir get() = hostString("additionalToolsDir")
    val absoluteAdditionalToolsDir get() = absolute(additionalToolsDir)
}

interface MingwConfigurables : TargetableConfigurables, ClangFlags

interface GccConfigurables : TargetableConfigurables, ClangFlags {
    val gccToolchain get() = hostString("gccToolchain")
    val absoluteGccToolchain get() = absolute(gccToolchain)

    val libGcc get() = targetString("libGcc")!!
    val dynamicLinker get() = targetString("dynamicLinker")!!
    val abiSpecificLibraries get() = targetList("abiSpecificLibraries")
}

interface AndroidConfigurables : TargetableConfigurables, ClangFlags

interface WasmConfigurables : TargetableConfigurables, ClangFlags, LldFlags

interface ZephyrConfigurables : TargetableConfigurables, ClangFlags {
    val boardSpecificClangFlags get() = targetList("boardSpecificClangFlags")
    val targetCpu get() = targetString("targetCpu")
    val targetAbi get() = targetString("targetAbi")
}