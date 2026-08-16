document.addEventListener('DOMContentLoaded', () => {
    const chartElements = document.querySelectorAll('[data-chart]');
    chartElements.forEach((element) => {
        const chartType = element.dataset.chart;
        const rawData = JSON.parse(element.dataset.labels || '{}');
        const labels = Object.keys(rawData);
        const values = Object.values(rawData);
        if (!labels.length) {
            return;
        }
        new Chart(element, {
            type: chartType,
            data: {
                labels,
                datasets: [{
                    label: 'Churn %',
                    data: values,
                    borderWidth: 1,
                    backgroundColor: ['#17365f', '#18a0fb', '#dc2626', '#d97706']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: chartType !== 'bar' } }
            }
        });
    });
});
