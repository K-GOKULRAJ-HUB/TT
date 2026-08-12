document.addEventListener('DOMContentLoaded', () => {
    // 1. Auto-hide alert messages after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s ease';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // 2. Client-side Password Match Check for Registration
    const regForm = document.querySelector('#registerForm');
    if (regForm) {
        regForm.addEventListener('submit', (e) => {
            const pwd = document.querySelector('#password').value;
            const confirmPwd = document.querySelector('#confirmPassword').value;
            const errorDiv = document.querySelector('#passwordError');

            if (pwd !== confirmPwd) {
                e.preventDefault();
                if (errorDiv) {
                    errorDiv.textContent = 'Passwords do not match.';
                    errorDiv.style.display = 'block';
                } else {
                    alert('Passwords do not match.');
                }
            }
        });
    }

    // 3. Interactive Star Rating Selector
    const starContainer = document.querySelector('.star-rating-input');
    if (starContainer) {
        const stars = starContainer.querySelectorAll('.star-icon');
        const ratingInput = document.querySelector('#ratingInput');

        stars.forEach(star => {
            star.addEventListener('click', () => {
                const val = star.getAttribute('data-value');
                ratingInput.value = val;
                stars.forEach(s => {
                    if (s.getAttribute('data-value') <= val) {
                        s.classList.add('selected');
                    } else {
                        s.classList.remove('selected');
                    }
                });
            });
        });
    }
});
