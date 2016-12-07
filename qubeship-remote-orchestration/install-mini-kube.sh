#!/bin/bash
# Get the Kernel Name
Kernel=$(uname -s)
case "$Kernel" in
    Linux)  Kernel="linux"              ;;
    Darwin) Kernel="darwin"                ;;
#    FreeBSD)    Kernel="freebsd"            ;;
* ) echo "Your Operating System -> ITS NOT SUPPORTED"   ;;
esac

#echo
#echo "Operating System Kernel : $Kernel"
#echo
# Get the machine Architecture
Architecture=$(uname -m)
case "$Architecture" in
  #  x86)    Architecture="x86"                  ;;
 #   ia64)   Architecture="ia64"                 ;;
  #  i?86)   Architecture="x86"                  ;;
    amd64)  Architecture="amd64"                    ;;
    x86_64) Architecture="amd64"                   ;;
#    sparc64)    Architecture="sparc64"                  ;;
* ) echo    "Your Architecture '$Architecture' -> ITS NOT SUPPORTED."   ;;
esac

osarch="$Kernel/$Architecture"

#echo
#echo "Operating System Architecture : $Architecture"
#echo