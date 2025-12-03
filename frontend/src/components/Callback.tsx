import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const Callback: React.FC = () => {
    const [error, setError] = useState<string | null>(null);
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const exchangeCodeForToken = async (code: string) => {
            try {
                const codeVerifier = sessionStorage.getItem('code_verifier');
                if (!codeVerifier) {
                    throw new Error('Code verifier not found.');
                }

                const params = new URLSearchParams();
                params.append('grant_type', 'authorization_code');
                params.append('code', code);
                params.append('redirect_uri', import.meta.env.VITE_REDIRECT_URI);
                params.append('client_id', import.meta.env.VITE_CLIENT_ID);
                params.append('code_verifier', codeVerifier);

                const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/oauth2/token`, params, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });

                const { access_token, refresh_token } = response.data;
                localStorage.setItem('access_token', access_token);
                localStorage.setItem('refresh_token', refresh_token);
                navigate('/');
            } catch (err) {
                setError('Failed to exchange code for token.');
            }
        };

        const queryParams = new URLSearchParams(location.search);
        const code = queryParams.get('code');

        if (code) {
            exchangeCodeForToken(code);
        } else {
            setError('No authorization code found.');
        }
    }, [location, navigate]);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return <div>Loading...</div>;
};

export default Callback;
