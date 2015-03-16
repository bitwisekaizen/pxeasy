accepteula
install --firstdisk --overwritevmfs
rootpw ***REMOVED***
network --bootproto=static --device=vmnic0 --ip=10.100.208.29 --gateway=10.100.208.1 --netmask=255.255.254.0 --hostname=gavin-test --nameserver=10.100.20.11,10.100.20.12
reboot

%firstboot --interpreter=busybox

esxcli network ip set --ipv6-enabled=false

# Enable SSH and the ESXi Shell
vim-cmd hostsvc/enable_ssh
vim-cmd hostsvc/start_ssh
vim-cmd hostsvc/enable_esx_shell
vim-cmd hostsvc/start_esx_shell

esxcli system maintenanceMode set -e true
esxcli software vib install -v http://pxeboot.infinio.com/vibs/esx-tools-for-esxi-9.7.0-0.0.00000.i386.vib -f

esxcli system shutdown reboot -r "rebootingAfterHostConfigurations"

esxcli system maintenanceMode set -e false
