PR = "r9"

DESCRIPTION = "ti-apps-launcher service"
HOMEPAGE = "https://github.com/TexasInstruments/ti-apps-launcher"

COMPATIBLE_MACHINE = "am62xx"

LICENSE = "TI-TFL"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=5c3a7f5f6886ba6f33ec3d214dc7ab4c"

DEPENDS = "qtbase qtquick3d qtmultimedia"
RDEPENDS:${PN} = "qtquick3d qtmultimedia bash"

BRANCH = "master"
SRCREV = "d6ce824480d76862ba3bdfead3aae82d2dad39ad"

SRC_URI = " \
    git://github.com/TexasInstruments/ti-apps-launcher.git;protocol=https;branch=${BRANCH} \
    file://ti-apps-launcher.service \
"

S = "${WORKDIR}/git"

APPS_DEFINES = ""
APPS_DEFINES:am62xx-evm = "SOC_AM62"
APPS_DEFINES:am62xx-lp-evm = "SOC_AM62_LP"

CONFIG_FILE = ""
CONFIG_FILE:am62xx-evm = "am62xx-evm"
CONFIG_FILE:am62xx-lp-evm = "am62xx-lp-evm"

inherit qmake5 deploy systemd

SYSTEMD_PACKAGES = "${PN}"

SYSTEMD_SERVICE:${PN} = "ti-apps-launcher.service"

do_compile:prepend() {
    echo "SOURCES += configs/${CONFIG_FILE}.cpp" >> ${S}/ti-apps-launcher.pro
    echo >> ${S}/ti-apps-launcher.pro
    echo "DEFINES += ${APPS_DEFINES}" >> ${S}/ti-apps-launcher.pro
}

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ti-apps-launcher ${D}${bindir}/ti-apps-launcher

    install -d ${D}/opt
    install -m 0755 ${S}/scripts/* ${D}/opt/ti-apps-launcher/

    install -d ${D}${sysconfdir}/systemd/system
    install -m 0755 ${WORKDIR}/ti-apps-launcher.service ${D}${sysconfdir}/systemd/system/ti-apps-launcher.service
}

FILES:${PN} = " \
    ${bindir}/ti-apps-launcher \
    /opt/ti-apps-launcher/ \
    ${sysconfdir}/systemd/system/ti-apps-launcher.service \
"
