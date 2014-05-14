# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

$script = <<SCRIPT
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9
sudo sh -c "echo deb https://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list"
sudo apt-get update
sudo apt-get install -y lxc-docker
sudo usermod -G docker vagrant
sudo apt-get install -y openjdk-7-jre curl
sudo curl -s http://repo.scala-sbt.org/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.13.1/sbt.deb -o /tmp/sbt.deb
sudo dpkg --install /tmp/sbt.deb
sudo apt-get install -y nginx
sudo rm /etc/nginx/nginx.conf
sudo ln -s /vagrant/frontend/nginx.conf /etc/nginx/nginx.conf
#sudo ln -s /opt/public /vagrant/frontend/public
sudo service nginx restart
SCRIPT

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "ubuntu/trusty64"

  config.vm.provision "shell", inline: $script

  config.vm.network "private_network", type: "dhcp"

  config.vm.provider "virtualbox" do |v|
    v.memory = 1024
    v.cpus = 2
  end
end
