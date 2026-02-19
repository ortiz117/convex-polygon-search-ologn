<script lang="ts">
    import { Target, Trash2, HelpCircle, Activity, MousePointer2 } from 'lucide-svelte';

    let vertices: {x: number, y: number}[] = [];
    let testPoint: {x: number, y: number} | null = null;
    let result: { inside: boolean; message: string } | null = null;
    let loading = false;
    
    // CAMBIO 1: Variable para controlar qué algoritmo usamos
    let usePdfAlgorithm = false;

    // Manejar clics en el plano
    function handleCanvasClick(e: MouseEvent) {
        const svg = e.currentTarget as SVGSVGElement;
        const rect = svg.getBoundingClientRect();
        const x = Math.round(e.clientX - rect.left);
        const y = Math.round(e.clientY - rect.top);

        // Lógica para el pentágono (5 puntos) según ejercicio
        if (vertices.length < 5) {
            vertices = [...vertices, { x, y }];
        } else if (!testPoint) {
            testPoint = { x, y };
            checkPoint();
        }
    }

    async function checkPoint() {
        if (!testPoint || vertices.length < 3) return;
        loading = true;
        try {
            // CAMBIO 2: Seleccionamos la URL dependiendo del switch
            const endpoint = usePdfAlgorithm ? '/api/polygon/check-pdf' : '/api/polygon/check';
            
            const res = await fetch(`http://localhost:8080${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ vertices, point: testPoint }) 
            });
            result = await res.json();
        } catch (err) {
            console.error("Error conectando al backend bro:", err);
        } finally {
            loading = false;
        }
    }

    // CAMBIO 3: Magia de Svelte. Si cambias el switch y ya hay un punto, recalcula solo.
    function handleToggle() {
        if (testPoint) {
            checkPoint();
        }
    }

    function reset() {
        vertices = [];
        testPoint = null;
        result = null;
    }
</script>

<div class="min-h-screen bg-slate-950 text-slate-100 p-4 md:p-8 font-sans selection:bg-cyan-500/30">
    <header class="max-w-5xl mx-auto mb-10 flex flex-col md:flex-row justify-between items-center gap-4">
        <div class="relative">
            <div class="absolute -inset-1 bg-gradient-to-r from-cyan-600 to-blue-600 rounded-lg blur opacity-25"></div>
            <div class="relative">
                <h1 class="text-4xl font-black tracking-tight bg-gradient-to-r from-cyan-400 via-blue-400 to-purple-500 bg-clip-text text-transparent">
                    GEOMETRIC-O(log N)
                </h1>
                <p class="text-slate-500 font-mono text-sm tracking-widest uppercase">Analizador Geometrico</p>
            </div>
        </div>
        
        <button on:click={reset} class="group flex items-center gap-2 bg-slate-900 hover:bg-red-950/30 px-6 py-3 rounded-xl transition-all border border-slate-800 hover:border-red-500/50 shadow-lg">
            <Trash2 size={18} class="group-hover:text-red-500 transition-colors" /> 
            <span class="font-bold">RESET SYSTEM</span>
        </button>
    </header>

    <main class="max-w-5xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-8">
        <div class="lg:col-span-4 space-y-6">
            <section class="bg-slate-900/50 backdrop-blur-xl p-6 rounded-3xl border border-white/5 shadow-2xl">
                <h2 class="text-xl font-bold mb-6 flex items-center gap-3 border-b border-white/5 pb-4">
                    <Activity size={22} class="text-cyan-400" /> STATUS
                </h2>
                
                <div class="space-y-4">
                    <div class="flex items-center justify-between p-3 bg-slate-950/50 rounded-xl border border-white/5">
                        <span class="text-xs text-slate-400 font-mono">
                            Algo: <span class="text-cyan-400">{usePdfAlgorithm ? 'PDF (Translación)' : 'Clásico (Vectores)'}</span>
                        </span>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" bind:checked={usePdfAlgorithm} on:change={handleToggle} class="sr-only peer">
                            <div class="w-9 h-5 bg-slate-800 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-slate-300 after:border-gray-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-cyan-500"></div>
                        </label>
                    </div>

                    <div class="flex justify-between items-center p-3 bg-slate-950/50 rounded-xl border border-white/5">
                        <span class="text-slate-400 text-sm">Vértices:</span>
                        <span class="font-mono text-cyan-400 font-bold">{vertices.length} / 5</span>
                    </div>

                    {#if vertices.length < 5}
                        <div class="flex items-center gap-3 text-amber-400 bg-amber-400/10 p-4 rounded-xl border border-amber-400/20 text-sm animate-pulse">
                            <MousePointer2 size={18} />
                            <span>Haz clic para definir el polígono (CCW)</span>
                        </div>
                    {:else if !testPoint}
                        <div class="flex items-center gap-3 text-cyan-400 bg-cyan-400/10 p-4 rounded-xl border border-cyan-400/20 text-sm animate-bounce">
                            <Target size={18} />
                            <span>¡Listo! Coloca el punto de prueba</span>
                        </div>
                    {/if}

                    {#if result}
                        <div class="mt-6 p-6 rounded-2xl border-2 transition-all duration-500 {result.inside ? 'bg-emerald-500/10 border-emerald-500/50 shadow-[0_0_30px_rgba(16,185,129,0.2)]' : 'bg-rose-500/10 border-rose-500/50 shadow-[0_0_30px_rgba(244,63,94,0.2)]'}">
                            <p class="text-center font-black text-2xl mb-1 uppercase tracking-tighter italic">
                                {result.inside ? 'TARGET INSIDE' : 'OUT OF BOUNDS'}
                            </p>
                            <p class="text-center text-xs opacity-70 font-mono italic">{result.message}</p>
                        </div>
                    {/if}
                </div>
            </section>
        </div>

        <div class="lg:col-span-8 relative group">
            <div class="absolute -inset-1 bg-gradient-to-r from-cyan-500 to-purple-600 rounded-[2rem] blur opacity-10 group-hover:opacity-20 transition duration-1000"></div>
            <div class="relative aspect-video lg:aspect-square bg-slate-950 rounded-[2rem] border border-white/10 overflow-hidden shadow-inner cursor-crosshair">
                <svg class="w-full h-full" on:mousedown={handleCanvasClick}>
                    <defs>
                        <pattern id="grid" width="30" height="30" patternUnits="userSpaceOnUse">
                            <path d="M 30 0 L 0 0 0 30" fill="none" stroke="rgba(255,255,255,0.03)" stroke-width="0.5"/>
                        </pattern>
                        <radialGradient id="glow">
                            <stop offset="0%" stop-color="rgba(34, 211, 238, 0.2)" />
                            <stop offset="100%" stop-color="transparent" />
                        </radialGradient>
                    </defs>
                    <rect width="100%" height="100%" fill="url(#grid)" />
                    <circle cx="50%" cy="50%" r="40%" fill="url(#glow)" />

                    {#if vertices.length > 0}
                        <polygon 
                            points={vertices.map(v => `${v.x},${v.y}`).join(' ')} 
                            class="fill-cyan-500/10 stroke-cyan-400 stroke-[3] transition-all duration-700 drop-shadow-[0_0_8px_rgba(34,211,238,0.5)]"
                        />
                        
                        {#each vertices as v, i}
                            <g class="cursor-pointer">
                                <circle cx={v.x} cy={v.y} r="6" class="fill-slate-950 stroke-cyan-400 stroke-2" />
                                <text x={v.x+10} y={v.y-10} class="text-[12px] font-mono fill-cyan-400/50">V{i}</text>
                            </g>
                        {/each}
                    {/if}

                    {#if testPoint}
                        <g class="{loading ? 'animate-pulse' : ''}">
                            <circle 
                                cx={testPoint.x} 
                                cy={testPoint.y} 
                                r="12" 
                                class="animate-ping opacity-20 {result?.inside ? 'fill-emerald-400' : 'fill-rose-400'}"
                            />
                            <circle 
                                cx={testPoint.x} 
                                cy={testPoint.y} 
                                r="6" 
                                class="stroke-white stroke-2 shadow-2xl {loading ? 'fill-yellow-400' : result?.inside ? 'fill-emerald-400 shadow-emerald-500' : 'fill-rose-400 shadow-rose-500'}"
                            />
                        </g>
                    {/if}
                </svg>
            </div>
        </div>
    </main>
</div>

<style>
    svg {
        touch-action: none;
    }
</style>