const AuthManager = {
  DEV_DOMAIN: '',
  refreshTimer: null,
  isRefreshing: false,
  refreshSubscribers: [],

  getAccessToken() {
    return localStorage.getItem('accessToken');
  },

  setAccessToken(token) {
    localStorage.setItem('accessToken', token);
  },

  // Access Token 재발급 (중복 방지)
  async refreshAccessToken() {
    // 이미 갱신 중이면 대기
    if (this.isRefreshing) {
      return new Promise((resolve) => {
        this.refreshSubscribers.push(() => {
          resolve(this.getAccessToken());
        });
      });
    }

    this.isRefreshing = true;

    try {
      const response = await fetch(`${this.DEV_DOMAIN}/api/v1/auth/reissue`, {
        method: 'POST',
        credentials: 'include'
      });

      if (!response.ok) {
        throw new Error('Token refresh failed');
      }

      const data = await response.json();

      if (data.code === 'OK') {
        this.setAccessToken(data.data.accessToken);
        console.log('✅ Access Token 갱신 완료');

        // 대기 중인 요청들에게 알림
        this.refreshSubscribers.forEach(callback => callback());
        this.refreshSubscribers = [];

        // 자동 갱신 타이머 재설정
        this.startAutoRefresh();

        return data.data.accessToken;
      } else {
        throw new Error('Token refresh failed');
      }
    } catch (err) {
      console.error('❌ Token 갱신 실패:', err);
      this.logout();
      throw err;
    } finally {
      this.isRefreshing = false;
    }
  },
  // JWT 만료 시간 가져오기
  getTokenExpiration(token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000;
    } catch {
      return null;
    }
  },

  // 자동 갱신 타이머 시작
  startAutoRefresh() {
    this.stopAutoRefresh();

    const accessToken = this.getAccessToken();
    if (!accessToken) {
      return;
    }

    const expiration = this.getTokenExpiration(accessToken);
    if (!expiration) {
      return;
    }

    const now = Date.now();
    const timeUntilExpiration = expiration - now;

    // 만료 2분 전에 갱신 (120000ms)
    const refreshTime = timeUntilExpiration - 120000;

    if (refreshTime > 0) {
      console.log(`⏰ ${Math.floor(refreshTime / 1000)}초 후 자동 갱신 예정`);
      this.refreshTimer = setTimeout(() => {
        this.refreshAccessToken();
      }, refreshTime);
    } else if (timeUntilExpiration > 0) {
      // 2분 미만 남음 → 10초 후 갱신
      this.refreshTimer = setTimeout(() => {
        this.refreshAccessToken();
      }, 10000);
    } else {
      // 이미 만료 → 즉시 갱신
      this.refreshAccessToken();
    }
  },

  // 자동 갱신 타이머 중지
  stopAutoRefresh() {
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = null;
    }
  },

  logout() {
    this.stopAutoRefresh();
    localStorage.clear();
    window.location.href = '/view/login';
  },

  redirectToLogin() {
    this.stopAutoRefresh();
    localStorage.clear();
    alert('세션이 만료되었습니다. 다시 로그인해주세요.');
    window.location.href = '/view/login';
  }
};

