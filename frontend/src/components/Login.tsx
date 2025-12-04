import React from 'react';
import pkceChallenge from 'pkce-challenge';

const Login: React.FC = () => {
    const handleLogin = async () => {
        const challenge = await pkceChallenge();
        sessionStorage.setItem('code_verifier', challenge.code_verifier);

        const clientId = import.meta.env.VITE_CLIENT_ID;
        const redirectUri = import.meta.env.VITE_REDIRECT_URI;
        const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;
        const scope = 'openid pizza.read';
        const responseType = 'code';
        const codeChallenge = challenge.code_challenge;
        const authorizationUrl = `${apiBaseUrl}/oauth2/authorize?response_type=${responseType}&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&code_challenge=${codeChallenge}&code_challenge_method=S256`;
        window.location.href = authorizationUrl;
    };

    return (
        <div>
            <h2>Login</h2>
            <button onClick={handleLogin}>Login with Spring Authorization Server</button>
        </div>
    );
};

export default Login;
