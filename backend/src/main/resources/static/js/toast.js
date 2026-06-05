function showToast(message, duration = 1500) {
    // Create toast element
    const toast = document.createElement('div');
    
    if(message.includes('\n') || message.includes('<br')) {
        toast.innerHTML = message;
        toast.style.whiteSpace = 'pre-line';
    } else {
        toast.textContent = message;
        toast.style.whiteSpace = 'nowrap';
    }
    
    // Style the toast
    toast.style.position = 'fixed';
    toast.style.bottom = '30px';
    toast.style.left = '50%';
    toast.style.transform = 'translateX(-50%)';
    toast.style.backgroundColor = '#333';
    toast.style.color = 'white';
    toast.style.padding = '12px 24px';
    toast.style.borderRadius = '8px';
    toast.style.fontSize = '14px';
    toast.style.fontFamily = 'system-ui, sans-serif';
    toast.style.zIndex = '9999';
    toast.style.boxShadow = '0 2px 10px rgba(0,0,0,0.2)';
    toast.style.textAlign = 'center';
    
    // Add to body
    document.body.appendChild(toast);
    
    // Remove after duration
    setTimeout(() => {
        toast.remove();
    }, duration);
}