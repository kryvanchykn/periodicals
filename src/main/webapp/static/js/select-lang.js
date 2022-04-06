const select = document.querySelector('.lang-select');
const form = document.querySelector('.lang-form')

select.addEventListener('change', () => {
    form.submit();
});