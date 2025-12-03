import axios from 'axios';

const setupAxiosInterceptors = () => {
    axios.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            if (error.response?.status === 401) {
                localStorage.removeItem('access_token');
                localStorage.removeItem('refresh_token');
                window.location.href = '/login';
            }
            return Promise.reject(error);
        }
    );
};

export default setupAxiosInterceptors;
