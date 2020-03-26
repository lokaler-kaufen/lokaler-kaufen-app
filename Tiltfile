docker_build('backend-image', './backend/')

k8s_yaml('backend/src/test/kubernetes/backend.yaml')
k8s_resource('backend', port_forwards=8080)

k8s_yaml('backend/src/test/kubernetes/database.yaml')
k8s_resource('database', port_forwards=5432)
