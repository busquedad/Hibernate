// src/utils/auth.ts

/**
 * Decodes the JWT token from localStorage to extract user roles.
 * This is a lightweight implementation that does not verify the token's signature.
 * It should only be used for UI purposes, not for security decisions.
 *
 * @returns {string[]} An array of user roles, or an empty array if the token is not found or invalid.
 */
export const getUserRoles = (): string[] => {
  const token = localStorage.getItem('access_token');
  if (!token) {
    return [];
  }

  try {
    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) {
      return [];
    }

    const decodedPayload = atob(payloadBase64);
    const payload = JSON.parse(decodedPayload);

    // As per the backend configuration, roles are in a "roles" claim.
    return payload.roles || [];
  } catch (error) {
    console.error('Failed to decode JWT token:', error);
    return [];
  }
};
