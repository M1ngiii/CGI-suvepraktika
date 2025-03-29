<!-- Üldine stiil igal lehel -->
<script>
    export let data;
    import "../app.css";
</script>

<div class="min-h-screen bg-gray-50 text-gray-900">
    <header class="bg-sky-500 px-6 py-4 text-white text-xl sticky top-0 z-50 shadow-md">
        <div class="max-w-screen-xl mx-auto h-10 flex items-center">
            <a href="/" class="text-left font-bold text-2xl transition-all duration-200 hover:text-3xl hover:no-underline">
                Lendaja
            </a>
        </div>
    </header>

    <main class="max-w-5xl mx-auto p-4">
        <slot />
    </main>
</div>