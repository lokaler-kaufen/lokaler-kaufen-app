docker_build('backend-image', './backend/', only=['./build/libs/'])

docker_build('frontend-image', './frontend/', build_args={'node_env': 'development'},
    live_update=[
        sync('.', '/app'),
        run('cd /app && npm install', trigger=['./package.json', './package-lock.json']),
])

k8s_yaml('backend/src/test/kubernetes/backend.yaml')
k8s_resource('backend', port_forwards=8080)

k8s_yaml('backend/src/test/kubernetes/database.yaml')
k8s_resource('database', port_forwards=5432)

k8s_yaml('frontend/frontend.yaml')
k8s_resource('frontend', port_forwards=4200)
